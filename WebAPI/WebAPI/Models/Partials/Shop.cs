using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using WebAPI.Models.DTOs;
using WebAPI.Models.Helpers;

namespace WebAPI.Models.ORM
{
    partial class Shop
    {
        public static ShopDTO ToDTO(Shop item, int level)
        {
            if (item == null || level <= 0)
            {
                return null;
            }

            return new ShopDTO
            {
                DateCreated = item.DateCreated,
                DateDeleted = item.DateDeleted,
                DateModified = item.DateModified,
                Deleted = item.Deleted,
                Id = item.Id,
                Location = Location.ToDTO(item.Location, level - 1),
                Requests = Request.ToListDTO(item.Requests, level - 1),
                Reviews = Review.ToListDTO(item.Reviews, level - 1),
                Appointments = Appointment.ToListDTO(item.Appointments, level - 1),
                ChildShops = ToListDTO(item.ChildShops, level - 1),
                ParentShop = ToDTO(item.ParentShop, level - 1),
                ParentShopId = item.ParentShopId,
                Tokens = Token.ToListDTO(item.Tokens, level - 1),
                LegalName = item.LegalName,
                LocationId = item.LocationId,
                ShortName = item.ShortName,
                Vat = item.Vat,
                Email = item.Email,
                Password = Helper.PASSWORD_PLACEHOLDER,
                WorkDays = item.WorkDays,
                WorkHours = item.WorkHours,
                CarCapacity = item.CarCapacity,
                HourlyRate = item.HourlyRate
            };
        }

        public static ICollection<ShopDTO> ToListDTO(ICollection<Shop> list, int level)
        {
            if (list == null || level <= 0)
            {
                return null;
            }
            return list.Select(x => x.ToDTO(level)).ToList();
        }

        public ShopDTO ToDTO(int level)
        {
            return level > 0 ? new ShopDTO
            {
                DateCreated = DateCreated,
                DateDeleted = DateDeleted,
                DateModified = DateModified,
                Deleted = Deleted,
                Id = Id,
                Location = Location.ToDTO(Location, level - 1),
                Requests = Request.ToListDTO(Requests, level - 1),
                Reviews = Review.ToListDTO(Reviews, level - 1),
                Appointments = Appointment.ToListDTO(Appointments, level - 1),
                LegalName = LegalName,
                LocationId = LocationId,
                ShortName = ShortName,
                Vat = Vat,
                Email = Email,
                Password = Helper.PASSWORD_PLACEHOLDER,
                ChildShops = ToListDTO(ChildShops, level - 1),
                ParentShop = ToDTO(ParentShop, level - 1),
                ParentShopId = ParentShopId,
                Tokens = Token.ToListDTO(Tokens, level - 1),
                WorkDays = WorkDays,
                WorkHours = WorkHours,
                CarCapacity = CarCapacity,
                HourlyRate = HourlyRate
            }:null;
        }
    }

}